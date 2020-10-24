import React, { useEffect, useState } from 'react'
import { View, StyleSheet } from 'react-native'
import { FeedbackTypeEnums } from '../../Enums/FeedbackTypeEnums'
import { FontAwesome5 } from '@expo/vector-icons'

interface IFeedbackButtonProps {
  initialCount?: number;
  feedbackType: FeedbackTypeEnums
}

export const FeedbackButton = ({
  initialCount,
  feedbackType
}: IFeedbackButtonProps) => {
  const [count, setCount] = useState(0)


  useEffect(() => {
    setCount(initialCount)
  }, [initialCount])

  return (
    <View style={styles.feedbackButton}>
      <FontAwesome5.Button
        name={
          feedbackType === FeedbackTypeEnums.Like
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
