import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:pull_to_refresh/pull_to_refresh.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Chuck Norris Jokes',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: ChuckNorrisJokesScreen(),
    );
  }
}

class Joke {
  final String id;
  final String value;

  Joke({required this.id, required this.value});
}

class ChuckNorrisJokesScreen extends StatefulWidget {
  @override
  _ChuckNorrisJokesScreenState createState() => _ChuckNorrisJokesScreenState();
}

class _ChuckNorrisJokesScreenState extends State<ChuckNorrisJokesScreen> {
  RefreshController _refreshController = RefreshController();
  List<Joke> jokes = [];

  @override
  void initState() {
    super.initState();
    fetchJokes();
  }

  void fetchJokes() async {
    try {
      for (int i = 0; i < 10; i++) {
        final response = await http.get(Uri.parse('https://api.chucknorris.io/jokes/random'));
        if (response.statusCode == 200) {
          final responseData = json.decode(response.body);
          final joke = Joke(id: responseData['id'], value: responseData['value']);
          setState(() {
            jokes.add(joke);
          });
        }
      }
    } catch (e) {
      print('Error fetching jokes: $e');
    } finally {
      _refreshController.refreshCompleted();
    }
  }

  void _onRefresh() {
    jokes.clear();
    fetchJokes();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Chuck Norris Jokes'),
      ),
      body: Column(
        children: [
          Image.asset(
            'assets/chucknorris_logo_coloured_small.png',
            height: 100,
            fit: BoxFit.contain,
          ),
          Expanded(
            child: SmartRefresher(
              controller: _refreshController,
              enablePullDown: true,
              onRefresh: _onRefresh,
              child: ListView.builder(
                itemCount: jokes.length,
                itemBuilder: (BuildContext context, int index) {
                  return Card(
                    child: ListTile(
                      title: Text(jokes[index].value),
                    ),
                  );
                },
              ),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _onRefresh,
        child: Icon(Icons.refresh),
      ),
    );
  }
}
