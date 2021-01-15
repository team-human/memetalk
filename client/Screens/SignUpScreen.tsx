import { useApolloClient, useQuery } from '@apollo/client'
import React, { useEffect, useState } from 'react'
import {
    View,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    Image,
    Button,
    Modal,
    TouchableHighlight,
} from 'react-native'
import { REGISTER_USER } from '../Query/LoginQuery'
import { createStackNavigator } from '@react-navigation/stack';
import { setStorageItem } from '../Helper/Storage';
const logo = require("../Assets/logo.png")

export const SignUpScreen = ({ navigation }) => {
    const client = useApolloClient();
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [name, setName] = useState("")
    const [logoW, setLogow] = useState(0)
    const [logoH, setLogoh] = useState(0)
    const [isModalVisible, setIsModalVisible] = useState(true)


    useEffect(() => {
        Image.getSize(
            logo,
            (width, height) => { setLogow(width); setLogoh(height); },
            () => { });
    }, [])

    const Stack = createStackNavigator();
    return (
        <View style={styles.centeredView}>
            {
                <Modal
                    animationType="slide"
                    transparent={true}
                    visible={true}
                >
                    <View style={styles.centeredView}>
                        <View style={styles.modalView}>
                            <Text style={styles.modalText}>Hello World!</Text>

                            <TouchableHighlight
                                style={{ ...styles.openButton, backgroundColor: '#2196F3' }}
                                onPress={() => {
                                    setIsModalVisible(v => !v);
                                }}>
                                <Text style={styles.textStyle}>Hide Modal</Text>
                            </TouchableHighlight>
                        </View>
                    </View>
                </Modal>
            }
            <Image source={logo} style={{ width: logoW, height: logoH }} />
            <View style={styles.inputControl}>
                <View style={styles.inputView} >
                    <TextInput
                        style={styles.inputText}
                        placeholder="Email"
                        placeholderTextColor="#A5A5A5"
                        onChangeText={text => setEmail(text)} />
                </View>
                <View style={styles.inputView} >
                    <TextInput
                        secureTextEntry
                        style={styles.inputText}
                        placeholder="Password"
                        placeholderTextColor="#A5A5A5"
                        onChangeText={text => setPassword(text)} />
                </View>

                <View style={styles.inputView} >
                    <TextInput
                        secureTextEntry
                        style={styles.inputText}
                        placeholder="Name"
                        placeholderTextColor="#A5A5A5"
                        onChangeText={text => setName(text)} />
                </View>

                <TouchableOpacity style={styles.button}
                    onPress={async (e) => {
                        try {
                            const { data } = await client.mutate({
                                mutation: REGISTER_USER,
                                variables: {
                                    userInfo:
                                    {
                                        username: email,
                                        password: password,
                                        name: name
                                    }
                                },
                            });
                            await setStorageItem(
                                'userinfo',
                                JSON.stringify(data.createUser)
                            );
                            setIsModalVisible(true)
                            console.log(data.createUser)
                        } catch (error) {
                            setIsModalVisible(false)
                            console.log(error)
                        }
                    }}
                >
                    <Text style={styles.loginText}>註冊</Text>
                </TouchableOpacity>
            </View>
        </View >
    );
}


const styles = StyleSheet.create({
    container: {
        backgroundColor: 'white',
        flex: 1,
        alignItems: 'center'
    },
    inputControl: {
        width: '100%',
        flex: 1,
        alignItems: 'center'
    },
    inputView: {
        width: "30%",
        minWidth: 300,
        marginVertical: 5,
    },
    inputText: {
        height: 50,
        borderWidth: 0,
        borderBottomWidth: 1,
        outlineWidth: 0
    },
    forgot: {
        color: "#A5A5A5"
    },
    button: {
        width: "30%",
        minWidth: 300,
        backgroundColor: "#E83468",
        borderRadius: 25,
        height: 50,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 10,
    },
    loginText: {
        color: 'white'
    },
    centeredView: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 22,
    },
    modalView: {
        justifyContent: 'center',
        margin: 20,
        backgroundColor: 'white',
        borderRadius: 20,
        padding: 35,
        alignItems: 'center',
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.25,
        shadowRadius: 3.84,
        elevation: 5,
    },
    openButton: {
        backgroundColor: '#F194FF',
        borderRadius: 20,
        padding: 10,
        elevation: 2,
    },
    textStyle: {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
    },
    modalText: {
        marginBottom: 15,
        textAlign: 'center',
    },
});