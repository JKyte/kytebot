This project can be directly imported into eclipse via the following steps

## Importing into Eclipse IDE
1. Open Eclipse
2. In the "Package Explorer" view, right click and select the "Import" option
3. Select the "Git" -> "Projects from Git" option
4. Select the URI option
5. Paste in the kytebot URI --> "https://github.com/JKyte/kytebot.git"
6. Select the branch you want to clone (default is "master")
7. Eclipse should import everything and you're ready to begin contributing to the kytebot project

## Running the bot (from Eclipse)
1. Open the kytebot project in Eclipse
2. Open ./properties/production.properties
3. Edit the variables for 'nick', 'passwd', 'startchan', and 'ircserver'
4. Run the class as a Java application


## Current Sprint Goals (V1.0.1 sprint)
1. Implement last seen command
2. Improve logger configs
3. Update instructions for cloning the repo
4. Code quality check on GMP/AP


## Feature Backlog
0. Stabilize functionality on maven
1. Alerts based on IRC messages
2. (DONE) Trusted users, trusted user actions
3. Framework for automatic replies/kytebot commands
4. Use JavaFX for UI
5. Word cloud
6. Last Seen
7. Courier module
8. Streamline messages
9. (DONE) Configs for ajoin
10. Allow flexibility in kytebot command syntax
11. Refactor msg structure for better readability
