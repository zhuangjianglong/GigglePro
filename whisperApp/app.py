from flask import Flask,request,jsonify
import os
import whisper

# 预先加载
model = whisper.load_model('small')

app = Flask(__name__)

@app.route('/whisper', methods=["GET"])
def whisper():
    return "Hello whisper!"

@app.route('/whisper/stt', methods=["POST"])
def stt():
    payload = request.get_json()
    path = payload['path']
    # print(path)

    result = model.transcribe(path)
    # print(result['text'])

    return result

if __name__ == "__main__":
    port = os.environ.get("PORT", 9997)
    app.run(host="0.0.0.0", port=port, debug=True)