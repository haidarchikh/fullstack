fetch('/service')
    .then(response => response.json())
    .then(serviceList => {
        const listContainer = document.querySelector('#service-list');

        serviceList.forEach(service => {
            const li = document.createElement("li");
            li.appendChild(document.createTextNode(`${service.name}: ${service.status}: ${service.lastPoll}`));
            listContainer.appendChild(li);
        });
    });


document.querySelector('#post-service').onclick = () =>
    fetch('/service', {
        method: 'post',
        headers: { 'Accept': 'application/json, text/plain, */*', 'Content-Type': 'application/json' },
        body: JSON.stringify({
            name: document.querySelector('#name').value,
            url: document.querySelector('#url').value
        })
    }).then(() => location.reload());

// TODO: populate a table instead of reloading the whole page
setInterval(() => {
    if (document.querySelector('#name').value || document.querySelector('#url').value) { return; }

    location.reload();
}, 1000 * 10)