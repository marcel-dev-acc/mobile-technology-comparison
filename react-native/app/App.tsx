import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  Button,
  FlatList,
  ActivityIndicator,
  Image,
} from 'react-native';

interface Joke {
  id: string;
  value: string;
}

function App(): JSX.Element {
  const [jokes, setJokes] = useState([] as Joke[]);
  const [isLoading, setIsLoading] = useState(true);


  const fetchJokes = async () => {
    setIsLoading(true);

    try {
      setJokes([]);
      let newJokes: Joke[] = [];
      for (let i = 0; i < 10; i++) {
        const response = await fetch('https://api.chucknorris.io/jokes/random');
        const jsonData = await response.json();
        newJokes = [...newJokes, { id: jsonData.id, value: jsonData.value }];
      }
      setJokes(newJokes);
    } catch (error) {
      console.error(error);
    }

    setIsLoading(false);
  };

  useEffect(() => {
    fetchJokes();
  }, []);

  return (
    <SafeAreaView style={{ flex: 1, padding: 16 }}>
      <Image
        source={require("./assets/chucknorris_logo_coloured_small.png")}
        resizeMode="contain"
        style={{
          width: 150,
          height: 100,
          alignSelf: 'center',
          marginBottom: 10,
        }}
      />
      {isLoading ? (
        <ActivityIndicator size="large" color="#0000ff" />
      ) : (
        <Button title="Reload Jokes" onPress={fetchJokes} />
      )}

      <FlatList
        data={jokes}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={{
            paddingVertical: 8,
            borderBottomWidth: 1,
            borderColor: 'grey',
            marginVertical: 10,
            marginHorizontal: 5,
          }}>
            <Text>{item.value}</Text>
          </View>
        )}
      />
    </SafeAreaView>
  );
}

export default App;