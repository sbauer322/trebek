data from `https://www.reddit.com/r/datasets/comments/1uyd0t/200000_jeopardy_questions_in_a_json_file/`

Create an index: `curl -XPUT 'localhost:9200/trivia?pretty'`

Generate 'random' results: `http://stackoverflow.com/questions/25887850/random-document-in-elasticsearch`

Mapping notes:

- value can be "None" for Final Jeopardy questions.
- question can contain hyperlinks and other messy text things for pictures and video. Consider omitting?
- round is either "Jeopardy!","Double Jeopardy!","Final Jeopardy!" or "Tiebreaker"
- show_number is of format '4680'
- air_date is of format YYYY-MM-DD

```
curl -XPUT 'localhost:9200/trivia/_mapping/jeopardy' -d '{
  "jeopardy" : {
    "properties" : {
	  "category": {
	    "type": "string"
	  },
	  "air_date": {
	    "type": "date",
		"format": "YYYY-MM-DD"
      },
	  "question": {
	    "type": "string"
	  },
	  "value": {
	    "type": "string"
      },
	  "answer": {
	    "type": "string"
      },
	  "round": {
	    "type": "string"
	  },
	  "show_number": {
	    "type": "string"
      }
    }
  }
}'
```

Sample insert:

```
curl -XPOST 'localhost:9200/trivia/jeopardy/auto_id?pretty' -d '{
  "category": "HISTORY",
  "air_date": "2004-12-31",
  "question": "'For the last 8 years of his life, Galileo was under house arrest for espousing this man's theory'",
  "value": "$200",
  "answer": "Copernicus",
  "round": "Jeopardy!",
  "show_number": "4680"
}'
```

JQ processing of dataset:

Needs to:

- [x] remove wrapping single quote from values of question
- [x] shape into format acceptable to ES Bulk API
- [ ] strip out links but keep text describing contents (start with `<a href=`)
- [ ] line breaks in question (e.g., see question with "It is agreed that there are...only two solids, the pyramid and") (`<br />`)
- [ ] single quotes are escaped (e.g., see `Harry Potter and the Sorceror\'s Stone`)
- [ ] italics `<i> and </i>`

```
cat ./stream-to-es-test.json | jq --compact-output 'map({index: {_index: "trivia", _type: "jeopardy"}}, {category, air_date, question, value, answer, round, show_number, question: .question | .[1:-1]}) | .[]' > output.json
```

Should take care of `<br />` and `\'`:

```
map({index: {_index: "trivia", _type: "jeopardy"}}, {category, air_date, question, value, answer, round, show_number, question: .question | .[1:-1] | gsub("<br />"; "\n") | gsub("\\'"; "'")}) | .[]
```

To send file to ES via Bulk API:

```
curl -s -XPOST localhost:9200/trivia/jeopardy/_bulk --data-binary "@jeopardy_questions1_bulk_api.json"
```
