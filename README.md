Survata Android SDK
====================

# Requirements #

- Android 4.0 (API Version 14) and up
- android-support-v4.jar, r23 (Updated in 1.0.5)
- android-support-annotations.jar, r23 (Updated in 1.0.5)
- Volley Library (library-1.0.19.jar)(Updated in 1.0.5)

# Usage #

Please check out [demo app](https://github.com/Survata/survata-android-demo-app) for a real-life demo.

### Step 1

Add dependencies in `build.gradle`. 1.0.18 is the latest version.

```groovy
        dependencies {
            compile 'com.survata.android:Survata:1.0.18'
        }
        repositories {
            maven {
                url  "http://dl.bintray.com/survata/maven"
            }
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

Make sure to add Survata imports (ex. `import com.survata.Survey`)

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
            Date date = new Date();
            option.contentName = date.toString();
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

##### Explaining contentName
`option.contentName` enforces that there is one survey per respondent per contentName. For example, if using a survey to unlock a level in a game or an e-book, it allows the publisher to offload enforcing that unlocking to be permanent onto us.

For example, if there's a game and there's a level 7. If a person playing the game has already earned the survey for level 7, if they request a survey for level 7 again, it shows that they already earned it.

You can pass a timestamp as the contentName if you would like to handle the logic of content availability on your side.
 * If you do not set contentName, a user will only ever be able to take one survey.

##### Testing
There is a frequency cap on how many surveys we allow one day for a specific IP address. Thus while testing/developing, it might be frustrating to not see surveys appear after a couple of tries. You can bypass this in two ways.

####1. FIRST WAY: Using "testing" property

There is a property called **testing** which is a boolean that can be set to true. Below is a snippet of the previous code above that includes the testing property. This will bring up real surveys (that might take very long to answer, so look at the second way), but your responses are not recorded.

```java
    SurveyOption option = new SurveyOption(publisherId);
    option.testing = true;
    mSurvey = new Survey(option);
```

####2. SECOND WAY: Using a default survey with SurveyOption, "preview" property & demo survey preview id

There is a property called **preview** that allows you to set a default preview Id for a survey (thus, have a specific survey). We have a default short demo survey with just 3 questions at Survata that is perfect for testing that uses the preview id **5fd725139884422e9f1bb28f776c702d**. Here's some code as to show you how to integrate it:

```java
    SurveyOption option = new SurveyOption(publisherId);
    option.preview = "5fd725139884422e9f1bb28f776c702d";
```

### Step 5  

Show survey in WebView. Should be called after checkSurvey();
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
