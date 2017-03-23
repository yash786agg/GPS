# GPS
Turn GPS on programmatically and current location instantly with runtime permission.

## Demo
![Demo](screenshots/Demo.gif)

 ## Usage
   ### Step 1 : Add "GPS" Library to your Android project.

   1- Open your project in Android Studio.
   2- Download the library
  
       (Download a zip File archive to unzip)
    
   3- Create a folder "GPS" in your project.
   4- Copy and paste the Code to your AnimatedLoadingIndicator folder
   5- On the root of your project directory create/modify the settings.gradle file. It should contain something like the following:

      include 'MyApp', ':GPS'

   6- Go to File > Project Structure > Modules.
   7- App > Dependencies.
   8- Click on the more on the left green "+" button > Module dependency.
   9- Select "GPS Library".
   
    ### Step 2 : Add Code to your Project
    
    Add Google Location Play Services Library in your build.gradle file:
    
    compile 'com.google.android.gms:play-services-location:10.2.0'
    
    Note: "10.2.0" Version at the Time of code Uploading. It may vary when you implement in your Code as per latest 
    Google Location Play Services Library Version.
    
    Add the Below File in your Code:
    
    ![MainActivity](MainFile/MainActivity.java)
    
    #locationUpdates Method you will get the Latitude and Longitude value.
    
    #Overrided Method #onStop Method you need to disconnectGoogleApiClient to Stop getting the Latitude and Longitude value.
    
    Note:
    
        if(networkUtilObj != null)
        {
            networkUtilObj.disconnectGoogleApiClient();
        }
        
        Above Method is Used to Disconnect the Google Api Connection for getting the Latitude and Longitude value.
    
    
