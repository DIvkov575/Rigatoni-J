
TODO
---
- Random playlist / artist / song cycling
- looping/shuffle mode assurance
- Captcha page detection + handling
- Logging
- Push notifications
- Binary compilation
- Testing/Dev mode
- Headless

Deploy
---
mvn deploy:deploy-file\
-Dfile=target/Rigatoni-1.0.0-jar-with-dependencies.jar\
-DgroupId=org.divkov\
-DartifactId=rigatoni\
-Dversion=1.0.0\
-Dpackaging=jar\
-DrepositoryId=github\
-Durl=https://maven.pkg.github.com/divkov575/rigatoni-j

Installation
---
Chrome, assets/chromedriver, assets/accounts.json, src\
IPRoyal - whitelist runner's ip


Proxy Research
---
SmartProxy - (Require ID verification for target ips) \
Brightdata - (required authentication + issue with SSL) \
Oxylabs - actual scammers - (Restrict target ips) \
IPRoyal - valid as of now
