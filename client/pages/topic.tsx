import React, { useEffect, useState } from 'react'
import {
  FlatList,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Image,
  ActivityIndicator,
} from 'react-native'
import { HashTagList } from '../View/HashTag/HashTagList'
import { FeedbackButton } from '../View/Button/FeedbackButton'
import { FeedbackTypeEnums } from '../Enums/FeedbackTypeEnums'
import { GET_MEMES_BY_TAG, GET_POPULAR_TAGS } from '../Query/TopicsQuery'
import { useQuery } from '@apollo/client'
import { IHashTag } from '../Model/IHashTag'

// TODO: will just inject data from api
const testData = [
  {
    id: 'bd7acbea-c1b1-46c2-aed5-3ad53abb28ba',
    userName: '使用者十一號',
    userAvatarUrl: 'https://reactnative.dev/img/tiny_logo.png',
    postTime: '2020.07.01 22:00',
    discussTimes: 2,
    sharedTimes: 1,
  },
  {
    id: '3ac68afc-c605-48d3-a4f8-fbd91aa97f63',
    userName: '使用者十一號',
    userAvatarUrl: 'https://reactnative.dev/img/tiny_logo.png',
    postTime: '2020.07.01 22:00',
    discussTimes: 2,
    sharedTimes: 1,
  },
  {
    id: '58694a0f-3da1-471f-bd96-145571e29d72',
    userName: '使用者十一號',
    userAvatarUrl: 'https://reactnative.dev/img/tiny_logo.png',
    postTime: '2020.07.01 22:00',
    discussTimes: 2,
    sharedTimes: 1,
  },
]

const Item = ({ item, onPress, style }) => (
  <TouchableOpacity onPress={onPress} style={[styles.item, style]}>
    <View style={styles.itemHeader}>
      <View style={styles.itemHeaderAvatar}>
        <Image
          source={{
            uri: item.userAvatarUrl,
          }}
          style={{
            width: 50,
            height: 50,
            borderRadius: 50,
            marginRight: 8,
          }}
        />
        <Text>{item.userName}</Text>
      </View>
      <View>
        <Text style={{ color: '#9D9D9D' }}>{item.postTime}</Text>
        <Text style={{ color: '#9D9D9D' }}>
          {item.discussTimes}個討論 {item.sharedTimes}個分享
        </Text>
      </View>
    </View>
    <Image
      source={require('../assets/temp.png')}
      style={{
        height: 325,
        resizeMode: 'cover',
      }}
    />
    <View style={styles.tagContainer}>
      <Text style={styles.tag}>#李登輝</Text>
      <Text style={styles.tag}>#韓國瑜</Text>
      <Text style={styles.tag}>#時事</Text>
    </View>
    <View style={styles.feedbackButton}>
      <FeedbackButton feedbackType={FeedbackTypeEnums.Like} />
      <FeedbackButton feedbackType={FeedbackTypeEnums.Dislike} />
    </View>
  </TouchableOpacity>
)

export default function TopicScreen({ navigation }) {
  const [selectedId, setSelectedId] = useState(null)
  const [selectedTag, setSelectedTag] = useState('')

  const { loading: popularTagsIsLoading, data: popularTagsData } = useQuery(
    GET_POPULAR_TAGS
  )

  const onSelectTag = (hashTag: IHashTag) => {
    setSelectedTag(hashTag.text)
  }

  const { loading: memeTagsIsLoading, data: memeTagsData } = useQuery(
    GET_MEMES_BY_TAG,
    {
      skip: !popularTagsData?.popularTags,
      variables: {
        tag: selectedTag,
      },
    }
  )

  const hashtagList = popularTagsIsLoading
    ? []
    : popularTagsData.popularTags.map((tag: any) => {
        return {
          id: tag,
          text: tag,
        }
      })

  useEffect(() => {
    if (!selectedTag && hashtagList?.length > 0) {
      setSelectedTag(hashtagList[0].text)
    }
  })

  const renderItem = ({ item }) => {
    const backgroundColor = '#ffffff'

    return (
      <Item
        item={item}
        onPress={() => setSelectedId(item.id)}
        style={{ backgroundColor }}
      />
    )
  }

  return popularTagsIsLoading ? (
    <View style={[loadingStyles.container, loadingStyles.horizontal]}>
      <ActivityIndicator size="large" />
    </View>
  ) : (
    <>
      <HashTagList
        hashtagList={hashtagList}
        showPoundSign={false}
        onSelectionCallBack={onSelectTag}
      />
      <SafeAreaView style={styles.container}>
        <FlatList
          data={testData}
          renderItem={renderItem}
          keyExtractor={(item) => item.id}
          extraData={selectedId}
        />
      </SafeAreaView>
    </>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
  },
  item: {
    padding: 20,
    marginVertical: 1,
    marginHorizontal: 0,
    display: 'flex',
  },
  itemHeader: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  itemHeaderAvatar: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
  },
  actionContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  actionButton: {
    width: 161,
    height: 45,
    backgroundColor: '#FFFFFF',
    borderRadius: 30,
    shadowColor: '#00000026',
  },
  tagContainer: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
  },
  tag: {
    borderRadius: 30,
    width: 89,
    backgroundColor: '#C4C4C4',
    textAlign: 'center',
    paddingVertical: 8,
    marginRight: 14,
  },
  feedbackButton: {
    display: 'flex',
    flex: 1,
    flexDirection: 'row',
  },
})

const loadingStyles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  horizontal: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 10,
  },
})
