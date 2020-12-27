import React from 'react'
import { View, Text, StyleSheet } from 'react-native'
import { IUserProps, User } from '../User/User'

export interface IProfileProps extends IUserProps{
}
export const Profile = ({ id, customizedStyles, imageUrl, name }: IProfileProps) => {
  return (
    <View style={styles.container}>
        <User id={id} name={name} customizedStyles={styles}/>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    backgroundColor: 'white'
  },
  user: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    flex: 1,
    margin: 5
  }
})
