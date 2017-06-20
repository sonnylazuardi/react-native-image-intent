/**
 * Created by Sonny Lazuardi on 06/20/17.
 */

var ImageIntent = require('react-native').NativeModules.ImageIntent;
var Platform = require('react-native').Platform;

module.exports = {
  getImageIntentBase64() {
    if (Platform.OS !== 'android') {
      throw Error('⚠️ Android only supported ⚠️');
    }
    return ImageIntent.getImageIntentBase64();
  }
};
