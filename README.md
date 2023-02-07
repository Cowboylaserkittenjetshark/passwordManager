# passwordManager
A password manager written in Java for the IB SL Internal Assessment

## Building
1. Clone the repository: `git clone https://github.com/Cowboylaserkittenjetshark/passwordManager.git`
2. Change to cloned repo: `cd passwordManager`
3. Run gradle build task
    - Linux: `./gradlew build`
    - Windows: `./gradlew.bat build`
## Running
The build task outputs a `.tar` and `.zip` archive to `passwordManager/app/build/distributions/`
1. Extract one of the above archives
    - For `.tar`: `tar -xf app.tar`
    - For `.zip`: Whatever works
      - `tar` preserves the execute permission on the launch script. If using the zip archive, the launch script may need to be made executable depending on your operating system
2. Run the launch script:
    - Linux: `./app/bin/app`
    - Windows: `./app/bin/app.bat`
