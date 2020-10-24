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
import { Meme } from '../Generated/graphqlType'
import { User } from '../View/User/User'
import { MemeInfo } from '../View/Info/MemeInfo'
import { MemeImage } from '../View/Picture/MemeImage'


const Item = ({ item, onPress, style }: {
  item: Meme,
  onPress: (value?: any) => void,
  style: { [index: string]: string }
}) => (
    <TouchableOpacity onPress={onPress} style={[styles.item, style]}>
      <View style={styles.memeHeaderInfo}>
        <User id={item?.author?.id} name={item?.author?.name} />
        <MemeInfo creationTime={item?.createTime ?? ""} />
      </View>
      <View>
        <MemeImage imageUrl={item?.url} />
      </View>
      <View>
        <HashTagList
          hashtagList={item?.tags?.map(tag => { return { id: tag, text: tag } }) ?? []}
          showPoundSign={false}
        />
      </View>
      <View style={styles.feedbackButton}>
        <FeedbackButton initialCount={item?.counter?.likeCount ?? 0} feedbackType={FeedbackTypeEnums.Like} />
        <FeedbackButton initialCount={item?.counter?.dislikeCount ?? 0} feedbackType={FeedbackTypeEnums.Dislike} />
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


  const renderItem = ({ item }: Meme) => {
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
            data={memeTagsData?.memesByTag ?? []}
            renderItem={renderItem}
            keyExtractor={(item: Meme) => item.id}
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
  memeHeaderInfo: {
    display: 'flex',
    flex: 1,
    flexDirection: 'row',
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
