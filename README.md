# Dixtro

Discord music bot made using Java Spring Boot and Discord4j.
this bot purpose was to create an open source discord music bot
so everyone could just host their own version of dixtro, and not rely on
some other bot around discord where you don't even know how is it handling
things.

This template is totally re-usable and editable, and you can use it however you want.
Don't forget and star this repository and why not follow me too :)

## How to run
Before hosting your bot or launching it locally whenever you feel like using it
you need to set up some things first, one of them is installing java21
and maven, after that you can just choose an IDE of your choice like IntelliJ, Netbeans or Eclipse.

After having all the required software the last step you need to do is to go
and get your key from the [discord developer portal](https://discord.com/developers/applications), you'll also
need to get a server where you can test beta features and get the server id where you'll invite the bot
and last but not less important your YouTube data api key, which you can get [here](https://console.cloud.google.com), this keys
need to be inserted in a file "secrets.properties" inside the src/main/resources folder, these values are
important and can get your bot into trouble if someone else gets them, so please guard them.

After getting all the values and inserting them in the file the last step is to run the project and see if it's working properly
be sure of debug anything that is working in a different way than it's supposed, fix the bugs or ask for help in my repository
creating an issue for example, if the project does work perfectly fine you can just go and install a .jar with this command:

```bash
mvn clean install
```

With this command you will generate a .jar file in your target folder, after that you'll only need to run this .jar file
following a command like:

```bash
java -jar dixtro-1.0.0.jar
```

And if you want to run it as a background file you can also use this one:

```bash
javaw -jar dixtro-1.0.0.jar
```