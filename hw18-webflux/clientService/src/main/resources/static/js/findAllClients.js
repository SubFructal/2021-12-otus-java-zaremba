const streamErr = e => {
    console.warn("error");
    console.warn(e);
}

fetch("http://localhost:8081/allClients").then((response) => {
    return can.ndjsonStream(response.body);
}).then(dataStream => {
    const reader = dataStream.getReader();
    const read = result => {
        if (result.done) {
            return;
        }
        render(result.value);
        reader.read().then(read, streamErr);
    }
    reader.read().then(read, streamErr);
});

const render = value => {
    const div = document.createElement('div');
    div.append('client:', JSON.stringify(value));
    document.getElementById('dataBlock').append(div);
};