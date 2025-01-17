import { ApolloError, useApolloClient } from '@apollo/client'
import React, { useEffect, useState } from 'react'
import {
    View,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    Image,
} from 'react-native'
import { SIGNUP_USER } from '../Query/UserQuery'
import { createStackNavigator } from '@react-navigation/stack';
import { setStorageItem } from '../Helper/Storage';
import { Modal } from 'react-native';
import { AuthContext } from '../Provider/AuthProvider';
import { CreateUserMutationResult } from '../Generated/graphqlType';
const logo = require("../assets/logo.png")

export const SignUpScreen = ({ navigation }) => {
    const client = useApolloClient();
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [name, setName] = useState("")
    const [logoW, setLogow] = useState(0)
    const [logoH, setLogoh] = useState(0)
    const [modalMsg, setModalMsg] = useState("test")
    const [isModalVisible, setIsModalVisible] = useState(false)

    const { signIn } = React.useContext(AuthContext);
    useEffect(() => {
        Image.getSize(
            logo,
            (width, height) => { setLogow(width); setLogoh(height); },
            () => { });
    }, [])

    const Stack = createStackNavigator();
    return (
        <View style={styles.container}>
            <Modal
                transparent={true}
                animationType="fade"
                visible={isModalVisible}
                style={styles.modal}
            >
                <View style={styles.centeredView}>
                    <View style={styles.modalView}>
                        <Text style={styles.modalText}>{modalMsg}</Text>
                        <TouchableOpacity
                            style={styles.openButton}
                            onPress={() => {
                                setIsModalVisible(false);
                            }}>
                            <Text>關閉</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
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
                        style={styles.inputText}
                        placeholder="Name"
                        placeholderTextColor="#A5A5A5"
                        onChangeText={text => setName(text)} />
                </View>

                <TouchableOpacity style={styles.button}
                    onPress={async (e) => {
                        try {
                            const { data } = await client.mutate({
                                mutation: SIGNUP_USER,
                                variables: {
                                    userInfo:
                                    {
                                        username: email,
                                        password: password,
                                        name: name
                                    }
                                },
                            }) as CreateUserMutationResult;
                            await setStorageItem(
                                'userinfo',
                                JSON.stringify(data?.createUser ?? null)
                            );
                            signIn({ email, password, token: data?.createUser?.token ?? null })
                            setModalMsg("註冊成功")
                            setIsModalVisible(true)
                        } catch (error: unknown) {
                            setModalMsg((error as ApolloError)?.message ?? JSON.stringify(error))
                            setIsModalVisible(true)
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
        borderBottomWidth: 1
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
        width: "100%",
        height: "100%",
        backgroundColor: 'white',
        margin: 0,
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    modal: {
        width: "100%",
        height: "100%",
        backgroundColor: 'white',
        margin: 0,
        justifyContent: 'center',
        alignItems: 'center',
    },
    modalView: {
        width: "100%",
        height: "100%",
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
        backgroundColor: '#2296F3',
        borderRadius: 20,
        padding: 10,
        elevation: 2,
    },
    modalText: {
        marginBottom: 15,
        textAlign: 'center',
    }
});