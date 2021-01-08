import React, { useEffect, useState } from 'react'
import {
  View,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  Image,
} from 'react-native'

const logo =  require("../assets/logo.png")

export default function LoginScreen({ navigation }) {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [logoW, setLogow] = useState(0)
    const [logoH, setLogoh] = useState(0)

    useEffect(() => {
        Image.getSize(
            logo, 
            (width, height) => {setLogow(width); setLogoh(height);}, 
            () => {});
      }, [])

    return (
        <View style={styles.container}>
            <Image source={logo} style={{ width: logoW, height: logoH }}/>
            <View style={styles.inputControl}>
                <View style={styles.inputView} >
                    <TextInput  
                    style={styles.inputText}
                    placeholder="Email" 
                    placeholderTextColor="#A5A5A5"
                    onChangeText={text => setEmail(text)}/>
                </View>
                <View style={styles.inputView} >
                    <TextInput  
                    secureTextEntry
                    style={styles.inputText}
                    placeholder="Password" 
                    placeholderTextColor="#A5A5A5"
                    onChangeText={text => setPassword(text)}/>
                </View>
                <TouchableOpacity style={styles.button} onPress={()=>{console.log("clicked!")}}>
                    <Text style={styles.loginText}>登入</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.button}>
                    <Text style={styles.loginText}>註冊</Text>
                </TouchableOpacity>
                <TouchableOpacity>
                    <Text style={styles.forgot}>Forgot your password?</Text>
                </TouchableOpacity>
            </View>
        </View>
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
    inputView:{
        width: "30%",
        minWidth: 300,
        marginVertical: 5,
    },
    inputText:{
        height: 50,
        borderWidth: 0,
        borderBottomWidth: 1,
        outlineWidth: 0
    },
    forgot:{
        color: "#A5A5A5"
    },
    button:{
        width:"30%",
        minWidth: 300,
        backgroundColor:"#E83468",
        borderRadius:25,
        height:50,
        alignItems:"center",
        justifyContent:"center",
        marginVertical: 10,
    },
    loginText:{
        color: 'white'
    }
});