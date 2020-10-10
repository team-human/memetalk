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
  onSelectionCallBack?: (item: IHashTag) => void
}

const Item = ({ item, onPress, style, showPoundSign }) => (
  <TouchableOpacity onPress={onPress} style={[styles.item, style]}>
    <HashTag style={styles.title} {...item} showPoundSign={showPoundSign} />
  </TouchableOpacity>
)

export const HashTagList = ({
  hashtagList,
  showPoundSign,
  onSelectionCallBack = (item: IHashTag): void => {},
}) => {
  const [selectedId, setSelectedId] = useState(null)

  const renderItem = ({ item }: { item: IHashTag }) => {
    const borderBottomColor = item.id === selectedId ? '#E83468' : '#FFFFFF'

    return (
      <Item
        showPoundSign={showPoundSign}
        item={item}
        onPress={() => {
          setSelectedId(item.id)
          onSelectionCallBack(item)
        }}
        style={{
          borderBottomWidth: 'thick',
          borderBottomColor,
          backgroundColor: '#FFFFFF',
        }}
      />
    )
  }

  return hashtagList?.length === 0 ? (
    <></>
  ) : (
    <SafeAreaView>
      <FlatList<IHashTag>
        style={styles.list}
        horizontal
        data={hashtagList}
        renderItem={renderItem}
        keyExtractor={(item) => item?.id}
        extraData={selectedId}
      />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
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
