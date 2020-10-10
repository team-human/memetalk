import React from 'react'
import { View, Text, Image } from 'react-native'
interface IUserProps {
  imageUrl?: string
  name?: string
}
export const User = ({ imageUrl, name }: IUserProps) => {
  return (
    <View>
      <Image source={{ uri: imageUrl }} />
      <Text>{name}</Text>
    </View>
  )
}
