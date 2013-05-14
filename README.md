flashy-android
Author: Adam Yabroudi
==============
The Android app is for clients on android phones to have better access to our product.


The layout of the code is activity based with lots of asynchronous tasks for all the JSON Requests.


MyJSON handles all JSON Requests. 
JSONThread is the asynchronous task that calls most of these JSON Requests as well as the other threads for saving resources (SaveResourceThread) and one to submit photos to the server (MIMEThread).

MainActivity_LogIn is the first activity that gets called when the app opens. If the user has logged in in a previous session they are taken through the DecksPage activity to the DeckListMaker. There the list of decks is presented for the user. 

If a user clicks a deck, they are taken to the ViewDeck activity.
If a user creates a new deck they are taken through a camera intent and then to the DrawLines activity which contains a DrawView. The DrawView has the lines on the photo and allows users to confirm the lines on which to break up the image. 

Row is a class created to help with the management of lines on the screen during the DrawLines activity. 

MyWebView extends WebView for specialized touchEvent responses. 

MyLogout is a common activity that allows logout from multiple areas within the app. 