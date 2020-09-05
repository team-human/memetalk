import React from 'react'
import {
  View,
  Text,
  NativeSyntheticEvent,
  NativeTouchEvent,
} from 'react-native'

interface IHashTagProps {
  showPoundSign?: boolean
  onPressCallback?: (ev: NativeSyntheticEvent<NativeTouchEvent>) => void
  text: string
}

export const HashTag = ({
  showPoundSign,
  text,
  onPressCallback = () => {},
}: IHashTagProps) => {
  return (
    <View>
      <Text onPress={onPressCallback}>{showPoundSign ? `#${text}` : text}</Text>
    </View>
  )
}
