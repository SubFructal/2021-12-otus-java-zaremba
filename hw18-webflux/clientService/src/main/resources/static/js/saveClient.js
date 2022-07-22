function saveClient() {
    const clientDataContainer = document.getElementById('clientDataContainer');
    const clientNameInput = document.getElementById("clientDtoName");
    const clientName = clientNameInput.value;
    const clientAddressInput = document.getElementById("clientDtoAddress");
    const clientAddress = clientAddressInput.value;
    const clientNumbersInput = document.getElementById("clientDtoNumbers");
    const clientNumbers = clientNumbersInput.value;
    fetch('/api/client', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({name: clientName, address: clientAddress, phoneNumbers: clientNumbers})
    })
    .then(response => response.json())
    .then(client => clientDataContainer.innerHTML = 'Клиент создан: ' + JSON.stringify(client));
}