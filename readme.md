react-native-image-intent
===============
[![npm version](https://badge.fury.io/js/react-native-image-intent.svg)](https://badge.fury.io/js/react-native-image-intent)

🔬 Image intent receiver for React Native android

## Installation
```
npm install --save react-native-image-intent
```

*Recommended via yarn*
```
yarn add react-native-image-intent
```

## Automatically link

### With React Native 0.27+
```
react-native link react-native-image-intent
```

### With older versions of React Native
You need [rnpm](https://github.com/rnpm/rnpm) (npm install -g rnpm)
```
rnpm link react-native-image-intent
```
*Hey, bro! react-native-image-intent wasn't support older version of React Native yet.*

### Setting Up AndroidManifest
*THIS IS AN IMPORTANT STEP*

```diff
<activity
    android:name=".MainActivity"
    android:label="@string/app_name"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
    android:windowSoftInputMode="adjustResize">
+   <intent-filter>
+       <action android:name="android.intent.action.SEND" />
+       <category android:name="android.intent.category.DEFAULT" />
+       <data android:mimeType="image/*" />
+   </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
...
```


## Manually link

### Android

- in `android/app/build.gradle`:

```diff
dependencies {
    ...
    compile "com.facebook.react:react-native:+"  // From node_modules
+   compile project(':react-native-image-intent')
}
```

- in `android/settings.gradle`:

```diff
...
include ':app'
+ include ':react-native-image-intent'
+ project(':react-native-image-intent').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-image-intent/android')
```

#### With React Native 0.29+

- in `MainApplication.java`:

```diff
+ import com.sonnylab.imageintent.ImageIntentPackage;

  public class MainApplication extends Application implements ReactApplication {
    //......

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
+         new ImageIntentPackage(),
          new MainReactPackage()
      );
    }

    ......
  }
```

#### With older versions of React Native:

- in `MainActivity.java`:

```diff
+ import com.sonnylab.imageintent.ImageIntentPackage;

  public class MainActivity extends ReactActivity {
    ......

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
+       new ImageIntentPackage(),
        new MainReactPackage()
      );
    }
  }
```

## Usage

### Base64

```javascript
// ES5
var ImageIntent = require('react-native-image-intent');
// or ES6
// import ImageIntent from 'react-native-image-intent';

ImageIntent.getImageIntentBase64().then((imageBase64) => {
  console.log('BASE64', imageBase64);
}).catch(e => console.log(e));

<Image style={{width: 100, height: 100}} source={{uri: `data:image/png;base64,${imageBase64}`}} />
```

### Image URL

```javascript
// ES5
var ImageIntent = require('react-native-image-intent');
// or ES6
// import ImageIntent from 'react-native-image-intent';

ImageIntent.getImageIntentUrl().then((imageUrl) => {
  console.log('IMAGE_URL', imageUrl);
}).catch(e => console.log(e));

<Image style={{width: 100, height: 100}} source={{uri: `file://${imageUrl}`}} />
```

## License
Copyright (c) [sonnylazuardi](https://github.com/sonnylazuardi). This software is licensed under the [MIT License](https://github.com/sonnylazuardi/react-native-image-intent/blob/master/LICENSE).
