import React from 'react'
import { View, Text, Image, StyleSheet } from 'react-native'
import { AntDesign } from '@expo/vector-icons';
interface IUserProps {
  id?: string;
  imageUrl?: string
  name?: string
}
export const User = ({ id, imageUrl, name }: IUserProps) => {
  return (
    <View style={styles.user}>
      {imageUrl ? <Image source={{ uri: imageUrl }} /> : <AntDesign name="user" size={36} color="black" />}
      <Text style={styles.name}>{name}</Text>
    </View>
  )
}


const styles = StyleSheet.create({
  user: {
    display: 'flex',
    flexDirection: 'row',
    flex: 1,
    margin: 5
  },
  name: {
    margin: 5,
    display: 'flex',
    alignItems: 'center'
  }
})