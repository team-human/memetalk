import React, { useState } from 'react'
import {
  SafeAreaView,
  Text,
  FlatList,
  StatusBar,
  StyleSheet,
  TouchableOpacity,
} from 'react-native'
import { HashTag } from './HashTag'
import { IHashTag } from '../../Model/IHashTag'

interface IHashTagListProps {
  hashtagList: IHashTag[]
  showPoundSign?: boolean
}

const Item = ({ item, onPress, style }) => (
  <TouchableOpacity onPress={onPress} style={[styles.item, style]}>
    <HashTag style={styles.title} {...item} showPoundSign />
  </TouchableOpacity>
)

export const HashTagList = ({ hashtagList, showPoundSign }) => {
  const [selectedId, setSelectedId] = useState(null)

  const renderItem = ({ item }) => {
    const borderBottomColor = item.id === selectedId ? '#E83468' : '#FFFFFF'

    return (
      <Item
        item={item}
        onPress={() => setSelectedId(item.id)}
        style={{
          borderBottomWidth: 'thick',
          borderBottomColor,
          backgroundColor: '#FFFFFF',
        }}
      />
    )
  }

  return (
    <SafeAreaView style={styles.container}>
      <FlatList
        style={styles.list}
        horizontal
        data={hashtagList}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        extraData={selectedId}
      />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
    // flex: 1,
    // backgroundColor: 'red',
  },
  list: {
    height: 50,
    flexGrow: 0,
  },
  item: {
    padding: 20,
  },
  title: {
    fontSize: 16,
  },
})
