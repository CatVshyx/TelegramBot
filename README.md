[bot icon]
<p align="center"><image src="https://user-images.githubusercontent.com/47032134/236805969-2bbc5658-bc5a-4e48-b1b5-daec601c8076.gif" width="200" height="200"></p>
# Telegram bot for FinanceFlow App

Bot made for help assistants to consult the clients. They don`t need to download additional programs/ register or login on websites  or that kind of this. Just open you telegram , print specified key to get access to the bot and finally start working! Users are waiting!

## Installation

-Clone the repository to your local folder [git clone https://github.com/CatVshyx/Client.git]


-Open command prompt in this folder and type 'mvn package' If you don`t have Maven, install it on [official website] (https://maven.apache.org/download.cgi) 

-Then you`ll have a built jar file with name 'name-jar-with-dependencies'

-To start the app, you can either: 
1. run it as a runnable jar file in command prompt[java -jar jar_file]
2. run as a project in IDE you have 

## Teck Stack

Language: Java

Build Tool: Maven

Libraries: TelegramBot API, Google Drive API, Google API Client, Jakarta Mail library

## Features

- Easier consulting with integration Telegram Bot as a connection between user and assistant. Here is used TG API, Dispatcher pattern, Chain-of-responsibility(handler) pattern.

- Implemented Email Sender Service WITH Jakarta Mail library and an account on SendieBlue.com which provides ease-of-use email sending    

- Set regular update of users` requests with a thread which updates a built-in class list of requests, and especially parse each of them. The request are taken from a txt file saved on Google Drive account. The file is updated by both server which sends requests there and bot which removes already answered requests.  Such purpose of dividing is made because of inability to save them with server 
## Screenshots

<p align="center">!
<img src="https://user-images.githubusercontent.com/47032134/236805969-2bbc5658-bc5a-4e48-b1b5-daec601c8076.gif" width="500" height="600">

</p>


The process of answering and sending request one of the users
