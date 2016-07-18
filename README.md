Survata Android SDK
====================

# Requirements #

- Android 4.0 (API Version 14) and up
- android-support-v4.jar, r23 (Updated in 1.0.5)
- android-support-annotations.jar, r23 (Updated in 1.0.5)
- Volley Library (library-1.0.19.jar)(Updated in 1.0.5)

# How to create aar and upload to jcenter #

1.  Register for an account on bintray.com

2.  Package aar and upload

    [bintray-release](https://github.com/novoda/bintray-release), it provides a=n easy way to release your Android and other artifacts to bintray.
    
    Refer the steps in [README.md](https://github.com/novoda/bintray-release/blob/master/README.md). 
    
    * modify `publish` closure in `build.gradle`
    
```groovy
         publish {
             userOrg = ''
             groupId = ''
             artifactId = ''
             publishVersion = ''
             desc = ''
             website = ''
         }
```
    
    * use the task `bintrayUpload` to publish (make sure you build the project first!):
    
```bash
    $ ./gradlew clean build bintrayUpload -PbintrayUser=BINTRAY_USERNAME -PbintrayKey=BINTRAY_KEY -PdryRun=false
```

3.  Sync bintray user repository to jcenter

    * You now have your own Maven Repository on Bintray which is ready to be uploaded to the library.
    
    [![ScreenShot](step1.png)](https://github.com/greycats/survata-android-sdk/blob/development/step1.png)

    * Nothing to do but click Send
    
    [![ScreenShot](step2.png)](https://github.com/greycats/survata-android-sdk/blob/development/step2.png)


    * It is pretty easy to sync your library to jcenter. Just go to the web interface and simply click at "Add to JCenter".
    Nothing we can do now but wait for 2-3 hours to let bintray team approves our request. Once sync request is approved, you will receive an email informing you the change. 
    
    [![ScreenShot](step3.png)](https://github.com/greycats/survata-android-sdk/blob/development/step3.png)

# Usage #

    Please check out [demo app](https://github.com/greycats/survata-android-demo) for a real-life demo.

### Step 1

Add dependencies in `build.gradle`.

```groovy
        dependencies {
            compile 'com.survata.android:library:1.0.10'
        }
```
### Step 2

Add permissions in `AndroidManifest.xml`

```
        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
        
        // optional, if you want to send zipcode
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```
    

### Step 3

Define Survey

```java
    private Survey mSurvey;
    
    private Button mSurveyButton;
    
    ...
    
```

### Step 4

Check survey availability. The publisherId property is `@NonNull`. 

```java
     public void checkSurvey() {
            Context context = getContext();
            SurveyOption option = new SurveyOption(publisherId);
            mSurvey = new Survey(option);
            mSurvey.create(getActivity(),
                    new Survey.SurveyAvailabilityListener() {
                        @Override
                        public void onSurveyAvailable(Survey.SurveyAvailability surveyAvailability) {
                            if (surveyAvailability == Survey.SurveyAvailability.AVAILABILITY) {
                                mSurveyButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
 ```

#### IMPORTANT NOTE

There is a frequency cap on how much surveys we allow one day for a specific IP address. Thus while testing/developing, it might be frustrating to not see surveys appear after a couple of tries. You can bypass this in two ways. 

####1. FIRST WAY: Using "testing" property

There is a property called **testing** which is a boolean that can be set to true. Below is a snippet of the previous code above that includes the testing property. This will bring up real surveys (that might take very long to answer, so look at the second way), but your responses are not recorded.

```java
    SurveyOption option = new SurveyOption(publisherId);
    option.testing = true;
    mSurvey = new Survey(option);
```

####2. SECOND WAY: Using a default survey with SurveyDebugOption, "preview" property & demo survey preview id 

There is another class called **SurveyDebugOption** (subclass of SurveyOption) in the SDK. It has a property called **preview** that allows you to set a default preview Id for a survey (thus, have a specific survey). We have a default short demo survey with just 3 questions at Survata that is perfect for testing that uses the preview id **5fd725139884422e9f1bb28f776c702d**. Here's some code as to show you how to integrate it: 

```java
    SurveyDebugOption option = new SurveyDebugOption(publisherId);
    option.preview = "5fd725139884422e9f1bb28f776c702d";
```

### Step 5  

Show survey in WebView. Should called after checkSurvey();
It will return the survey events (COMPLETED, SKIPPED, CANCELED, CREDIT_EARNED, NETWORK_NOT_AVAILABLE, NO_SURVEY_AVAILABLE).

```java
     private void showSurvey() {                
            mSurvey.createSurveyWall(getActivity(), new Survey.SurveyStatusListener() {
                    @Override
                    public void onResult(Survey.SurveyEvents surveyEvents) {
                        if (surveyEvents == Survey.SurveyEvents.COMPLETED) {
                              mSurveyButton.setVisibility(View.GONE);
                        }
                    }
                });
            }
```
