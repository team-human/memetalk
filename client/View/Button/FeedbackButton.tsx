import React, { useState } from 'react'
import { View, Text, StyleSheet } from 'react-native'
import { FeedbackTypeEnums } from '../../Enums/FeedbackTypeEnums'
import { FontAwesome5 } from '@expo/vector-icons'

interface IFeedbackButtonProps {
  feedbackType: FeedbackTypeEnums
}

export const FeedbackButton = ({
  feedbackType: feedbackTypeEnums,
}: IFeedbackButtonProps) => {
  const [count, setCount] = useState(0)

  return (
    <View style={styles.feedbackButton}>
      <FontAwesome5.Button
        name={
          feedbackTypeEnums === FeedbackTypeEnums.Like
            ? 'thumbs-up'
            : 'thumbs-down'
        }
        backgroundColor="#FFFFFF"
        onPress={() => setCount(count + 1)}
        size={24}
        color="black"
      ></FontAwesome5.Button>
      <View style={styles.count}>{count}</View>
    </View>
  )
}

const styles = StyleSheet.create({
  feedbackButton: {
    margin: 10,
    padding: 10,
    display: 'flex',
    flexDirection: 'row',
    flex: 1,
    justifyContent: 'space-between',
    borderColor: '#C4C4C4',
    borderWidth: 3,
    borderRadius: 50,
  },
  count: {
    margin: 10,
  },
})
