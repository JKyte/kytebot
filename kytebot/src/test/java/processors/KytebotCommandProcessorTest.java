package processors;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import botConfigs.IRCBotConfigs;
import processors.KytebotCommandProcessor;
import msg.ComplexMsg;
import msg.GenericMsg;

/**
 * 
 * @author JKyte
 *
 */
public class KytebotCommandProcessorTest {

	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQForTests;
	private KytebotCommandProcessor kcp;
	private IRCBotConfigs testConfigs;
	private static Logger log = LogManager.getLogger(KytebotCommandProcessorTest.class);

	@Before
	public void beforeTests(){
		testConfigs = new IRCBotConfigs();
		testConfigs.setStartChannel("#startChanForTests");
		testConfigs.setNick("nickForTests");
		testConfigs.setTrustedUsers("trustedUser_1,trustedUser_2");
		testConfigs.setStoryChans("#chanForTests");

		outboundMsgQForTests = new ConcurrentLinkedQueue<GenericMsg>();

		kcp = new KytebotCommandProcessor(outboundMsgQForTests, testConfigs, log);
	}

	@Test
	public void testProcessKytebotCmd_TrustedUser(){
		ComplexMsg msgForTest = new ComplexMsg("A", "B", "trustedUser_1", "D", "E", "F", "G", "H");

		boolean trusted = kcp.processKytebotCmd(msgForTest);

		Assert.assertEquals(true, trusted);
	}


	@Test
	public void testProcessMsgInternal(){
		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "tell me a story", "G", "H");

		kcp.processMsgInternal(msgForTest, false);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals(null, msgAfterTest );
	}

	@Test
	public void testProcessMsgInternal_else_condition1(){
		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "not a story command", "G", "H");

		kcp.processMsgInternal(msgForTest, false);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals(null, msgAfterTest );
	}

	@Test
	public void testProcessMsgInternal_else_condition2(){
		ComplexMsg msgForTest = new ComplexMsg("A", "nickForTests", "C", "D", "E", "not a story command", "G", "H");

		kcp.processMsgInternal(msgForTest, false);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals("PRIVMSG H :ACTION wishes he had the ability to talk", msgAfterTest.getData() );
	}

	@Test
	public void testGoTellAStory_condition1(){
		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "F", "G", "#chanForTests");

		kcp.goTellAStory(msgForTest, false);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals("PRIVMSG #chanForTests :ACTION knows a few stories, but isn't willing to share just yet.", msgAfterTest.getData() );
	}

	@Test
	public void testGoTellAStory_condition2(){
		ComplexMsg msgForTest = new ComplexMsg("A", "nickForTests", "C", "D", "E", "F", "G", "nickForTests");

		kcp.goTellAStory(msgForTest, true);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals("PRIVMSG nickForTests :ACTION knows a few stories, but isn't willing to share just yet.", msgAfterTest.getData() );
	}

	@Test
	public void testGoTellAStory_condition3(){
		//	This unit tests takes car of negative cases where nothing is expected
		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "F", "G", "H");

		kcp.goTellAStory(msgForTest, false);
		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals(null, msgAfterTest );

		kcp.goTellAStory(msgForTest, true);
		msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals(null, msgAfterTest );
	}

	@Test
	public void testsendUntrustedResponse(){

		kcp.sendUntrustedResponse("targetForTests");	
		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals("PRIVMSG targetForTests :ACTION wishes he had the ability to talk", msgAfterTest.getData() );
	}

	@Test
	public void testSendStoryReponse(){

		kcp.sendStoryResponse("targetForTests");	
		GenericMsg msgAfterTest = outboundMsgQForTests.poll();		
		Assert.assertEquals("PRIVMSG targetForTests :ACTION knows a few stories, but isn't willing to share just yet.", msgAfterTest.getData() );
	}

	@Test
	public void testProcessSideChat(){

		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "F", "G", "H");

		kcp.processSideChat(msgForTest);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();

		Assert.assertEquals(true, msgAfterTest.getData().startsWith("PRIVMSG ") );
	}

	@Test
	public void testExceptionThrownInProduction_1(){

		ComplexMsg msgForTest = new ComplexMsg("A", "B", "C", "D", "E", "kytebot tell a story please oh pretty please", "G", "H");

		kcp.processMsgInternal(msgForTest, false);

		GenericMsg msgAfterTest = outboundMsgQForTests.poll();
		Assert.assertEquals(null, msgAfterTest );

	}


	@After
	public void afterTests(){
		kcp = null;
		outboundMsgQForTests = null;
		log = null;
		testConfigs = null;
	}
}
