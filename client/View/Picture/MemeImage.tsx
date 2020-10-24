import React from 'react'
import { View, Image, StyleSheet } from 'react-native'
import { AntDesign } from '@expo/vector-icons';

interface IMemeImageProps {
  imageUrl?: string
}
export const MemeImage = ({
  imageUrl
}: IMemeImageProps) => {
  return (
    <View style={styles.container}>
      {/* {imageUrl ? <Image source={{ uri: imageUrl }} /> : <AntDesign name="picture" size={96} color="black" />} */}
      <AntDesign name="picture" size={300} color="black" />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-evenly'
  }
})
