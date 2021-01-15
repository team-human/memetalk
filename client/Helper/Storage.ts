import { Platform } from 'react-native';
import { SecureStore } from 'expo';
import AsyncStorage from '@react-native-community/async-storage';

export const setStorageItem = (key: string, value: string) => {
  if (Platform.OS === 'web') {
    return AsyncStorage.setItem(key, value);
  } else {
    return SecureStore.setItemAsync(key, value);
  }
}

export const getStorageItem = (key: string) => {
  if (Platform.OS === 'web') {
    return AsyncStorage.getItem(key);
  } else {
    return SecureStore.getItemAsync(key);
  }
}

