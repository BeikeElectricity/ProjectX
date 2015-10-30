ProjectX a.k.a. The Bus Factor
====

# Getting started

The repository is cloned by running:

    git clone git@github.com:BeikeElectricity/ProjectX.git

## Dependencies
- Java 7 SDK
- Android SDK
- A (virtual) android device

## Gradle
For gradle, and the project, to work properly you need to have the apikey for the api in a gradle.properties file. Either in the ProjectX/App folder or in your gradle home folder outside of the git root.  The location of the second file is:
- Unix: /home/username/.gradle/gradle.properties
- Windows: C:\Users\username\.gradle\gradle.properties

The content of the file should be:

    CYBERCOM_API_KEY=<insert key here>
You can add this at the end of the file if it already exists.
# Modifying and building
The project is best modified in Android Studio. The building process uses Gradle and is automated by Android Studio.
## Running / Building Release
Building the application can be done by running the gradle task assembleRelease either from within Android Studio or by running the command:
    
    gradle assembleRelease
For this to work you need to have ```release.properties``` file where the keystore for signing the app is configured. This file should be in the same folder as ```build.grade```. 
See ```release.properties.sample``` for reference on how it should be configured. 

Generating a new keystore can be done in Android studio at *Generate Signed APK* under *Build* in the menu. Then pressing the button for *Create new* under keystore path in the dialog.

For further information on signing the application, see the android developer manual.

The result of the grade command is a apk named ```app-release.apk``` in the folder ```App/app/build/outputs/apk```. If you build a signed named version, please move a copy of the apk to a new folder, named by the version, in *Releases*.

You can also make a release build by utilizing the Generate Signed APK mentioned earlier. The resulting apk will be named app-release.apk and will be placed in the App/app/ folder. 
# Testing
Testing is easiest done by by making a new Android Test run configuration in Android Studio
# Web Server
The server is intended to run on the LAMP stack,
so make sure you have that installed on your system.
For example using ubuntu run
     
    sudo apt-get update
    sudo apt-get install apache2 mysql-server php5 php5-mysql

When you have your webserver up and running you need to run the ```setup.sh``` script in the database folder, this creates a database ProjectX with the correct tables and a user beike with password beike which is granted all to said database.

After that you need to put the php scripts from the webservice directory in your document root and restart the apache2 service.

    sudo cp webservice/* /var/www/html
    sudo service apache2 restart
#License
See the `LICENSE` file in project root.
