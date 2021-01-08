import React, { useEffect, useState } from 'react'
import {
  Image,
  Platform,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  View,
} from 'react-native'
import * as ImagePicker from 'expo-image-picker';
import { TouchableOpacity } from 'react-native-gesture-handler';
import { Input } from '@ui-kitten/components';

export default function ComposeScreen() {
    const [image, setImage] = useState(null || String);

    useEffect(() => {
        (async () => {
        if (Platform.OS !== 'web') {
            const { status } = await ImagePicker.requestCameraPermissionsAsync();
            if (status !== 'granted') {
            alert('Sorry, we need camera roll permissions to make this work!');
            }
        }
        })();
    }, []);

    const pickImage = async () => {
      let result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.All,
        allowsEditing: true,
        aspect: [4, 3],
        quality: 1,
      });
  
      console.log(result);
  
      if (!result.cancelled) {
        setImage(result?.uri);
      }
    };

  return (
    <>
      <SafeAreaView style={styles.container}>
        <Text style={styles.title}>MEME</Text>
        <Text style={styles.subTitle}>選個主題發聲吧</Text>
        <View style={styles.uploadView}>
          <View style={styles.uploadBtnLayout} >
            <TouchableOpacity style={styles.uploadBtn} onPress={pickImage} >
              <Text style={styles.buttonText}> 上傳圖片發布心情 </Text>
            </TouchableOpacity>
          </View>
        </View>
        {
          image ? 
            <View style={styles.imageDetail}>
              <View style={styles.clearBtnLayout} >
                <TouchableOpacity style={styles.clearBtn} onPress={() => {setImage('')}} >
                  <Text style={styles.clearBtnText}> X </Text>
                </TouchableOpacity>
              </View>
              <View style={styles.inputLayout} >
                <Input placeholder='輸入標籤' />
                <Text style={styles.tag}>#職場 #霸凌 #狗 #貓奴</Text>
              </View>
              <Image source={{ uri: image }} style={ styles.previewImage} />
              <View style={styles.uploadBtnLayout} >
                <TouchableOpacity style={styles.uploadBtn} onPress={() => {}} >
                  <Text style={styles.buttonText}> 送出 </Text>
                </TouchableOpacity>
              </View>
            
            </View>
           : null
        }
      </SafeAreaView>
    </>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
    backgroundColor: '#ffffff',
    position: 'relative'
  },
  title: {
    textAlign: 'center',
    fontSize: 24,
    fontWeight: '700',
    fontStyle: 'normal',
    lineHeight: 28
  },
  subTitle: {
    marginTop: 12,
    textAlign: 'center',
    fontSize: 14,
    fontWeight: '400',
    fontStyle: 'normal',
    lineHeight: 16
  },
  uploadView: {
    margin: 20,
    borderStyle: 'dashed',
    borderWidth: 1,
    borderColor: 'black',
    flex: 1,
  },
  uploadBtnLayout: {
    position: 'absolute',
    left: '50%',
    top: '60%',
    transform: [{
      translateX: -75
    }]
  },
  uploadBtn: {
    width: 150,
    backgroundColor: '#C4C4C4',
    padding: 8,
  },
  previewImage: {
    width: 300, 
    height: 300,
    marginRight: 'auto',
    marginLeft: 'auto',
    marginTop: '10%',
    borderRadius: 10

  },
  buttonText: {
    textAlign: 'center'
  },
  imageDetail: {
    position: 'absolute',
    height: '100vh',
    width: '100vw',
    left: 0,
    top: 0,
    backgroundColor: '#F0F0F0',
    marginTop: '10%'
  },
  clearBtnLayout: {
    position: 'absolute',
    right: 20,
    top: 20
  },
  clearBtn: {
    width: 'auto',
    height: 'auto'
  },
  clearBtnText: {
    fontSize: 20,
    textAlign: 'center'
  },
  inputLayout: {
    width: '70%',
    marginTop: '10%',
    marginLeft: 'auto',
    marginRight: 'auto'
  },
  tag: {
    color: '#000000',
    opacity: 0.4,
    fontSize: 14,
    lineHeight: 17
  }

})
