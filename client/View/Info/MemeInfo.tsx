import React, { useEffect, useState } from 'react'
import { View, Text, StyleSheet } from 'react-native'
interface IMemeInfoProps {
  creationTime?: string
  discussionCount?: number
  shareCount?: number
}

const pluralize = require('pluralize')


export const MemeInfo = ({
  creationTime,
  discussionCount,
  shareCount,
}: IMemeInfoProps) => {
  const date = creationTime ? new Date(creationTime) : new Date()
  const [discussionNum, setDiscussionNum] = useState(0)
  const [shareNum, setShareNum] = useState(0)

  useEffect(() => {
    if (discussionCount) setDiscussionNum(discussionCount)
    if (shareCount) setShareNum(shareCount)
  }, [discussionCount, shareCount])

  const dateString = creationTime
    ? `${(date.getMonth() + 1)
      .toString()
      .padStart(2, '0')}/${date
        .getDate()
        .toString()
        .padStart(2, '0')}/${date
          .getFullYear()
          .toString()
          .padStart(4, '0')} ${date
            .getHours()
            .toString()
            .padStart(2, '0')}:${date
              .getMinutes()
              .toString()
              .padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
    : ''

  return (
    <View style={styles.container}>
      <View style={styles.date}>
        <Text>{dateString}</Text>
      </View>
      <View style={styles.number}>
        <Text>{`${discussionNum || 0} ${discussionNum > 1 ? pluralize("Discussion") : "Discussion"}  `}</Text>
        <Text>{`${shareNum || 0} ${shareNum > 1 ? pluralize("Share") : "Share"}  `}</Text>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    display: 'flex',
    flexDirection: 'column'
  },
  date: {
    display: 'flex',
    flexDirection: 'row-reverse',
    flex: 1,
  },
  number: {
    display: 'flex',
    flexDirection: 'row-reverse',
    flex: 1,
  }
})
