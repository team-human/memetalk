import { useQuery } from '@apollo/client'
import React, { useState } from 'react'
import {
  ActivityIndicator,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import { FlatGrid } from 'react-native-super-grid'
import { tagColorList } from '../Constants/Color'
import { GET_CURRENT_USER, GET_MEMES_BY_AUTHORID } from '../Query/TopicsQuery'
import { Profile } from '../View/Profile/Profile'

export const ProfileScreen = ({ navigation }) => {
  const [selectedId, setSelectedId] = useState(null)
  const { loading: currentUserIsLoading, data: currentUserData } = useQuery(
    GET_CURRENT_USER
  )
  const { loading: authMemeIsLoading, data: authMemeData } = useQuery(
    GET_MEMES_BY_AUTHORID,
    {
      skip: !currentUserData?.currentUser,
      variables: {
        userId: currentUserData?.currentUser?.id,
      },
    }
  )

  const memeLIst = authMemeIsLoading
    ? []
    : authMemeData
      ? authMemeData.memesByAuthorId.map((meme: any) => {
        return {
          id: meme.id,
          tags: meme.tags,
        }
      })
      : []

  const renderItem = ({ item }) => {
    const bgColor =
      tagColorList[Math.floor(Math.random() * Math.floor(tagColorList.length))]
    return (
      <TouchableOpacity onPress={() => setSelectedId(item.id)}>
        <View style={[styles.itemContainer, { backgroundColor: bgColor }]}>
          <Text style={styles.tags}>{item.tags.join(',')}</Text>
        </View>
      </TouchableOpacity>
    )
  }

  return (
    <>
      {currentUserIsLoading ? (
        <View style={[loadingStyles.container, loadingStyles.horizontal]}>
          <ActivityIndicator size="large" />
        </View>
      ) : (
          <Profile {...currentUserData?.currentUser} />
        )}
      {authMemeIsLoading ? (
        <View style={[loadingStyles.container, loadingStyles.horizontal]}>
          <ActivityIndicator size="large" />
        </View>
      ) : (
          <SafeAreaView style={styles.container}>
            <FlatGrid
              itemDimension={100}
              data={memeLIst}
              style={StyleSheet.flatten(styles.gridView)}
              fixed
              spacing={16}
              renderItem={renderItem}
            />
          </SafeAreaView>
        )}
    </>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: StatusBar.currentHeight || 0,
    backgroundColor: '#ffffff',
  },
  gridView: {
    marginTop: 10,
    flex: 1,
  },
  itemContainer: {
    justifyContent: 'flex-end',
    borderRadius: 5,
    padding: 10,
    height: 100,
  },
  tags: {
    fontSize: 16,
    color: '#fff',
    fontWeight: '600',
  },
  itemCode: {
    fontWeight: '600',
    fontSize: 12,
    color: '#fff',
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
