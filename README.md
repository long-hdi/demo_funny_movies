# demo_funny_movies
This project is for demo only. This is a fun website. Users can register, login and paste a youtube url into the share box. The server will process the url, extract the id and use the video id to call youtube api and fetch the video information. The database in use is postgresql. You must set up your Google API account and enable youtube api, then set an environment variable as GOOGLE_API_KEY=[your key]. When setting up your application server, you must set the default isolation level to repeatable read.

Tested on:
JDK 11, Payara 5.2020.5, PostgreSQL 12.4
