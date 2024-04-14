document.addEventListener('DOMContentLoaded', (event) => {
    const urls = document.querySelectorAll('.link')
    console.log(urls)


    urls.forEach(url =>
    {
        const id = url.getAttribute('data-id')
        const urlPreview = url.getAttribute('data-url')

        const data = {q: urlPreview}

        fetch('https://api.linkpreview.net', {
            method: 'POST',
            headers: {
                'X-Linkpreview-Api-Key': 'a1953d9b7a1283618650bcfb52dbd0bf',
            },
            mode: 'cors',
            body: JSON.stringify(data),
        }).then(res => {
            if (res.status != 200) {
                console.log(res.status)
                if (res.status === 425) {
                    console.error('The server responded with a status of 425 (Too Early). Please try again later.');
                } else {
                    throw new Error('something went wrong');
                }
            }
            return res.json()
        }).then(response => {
            const linkPreviewImage = document.querySelector('.a' + id);
            if(linkPreviewImage) {
                linkPreviewImage.src = response.image;
            }
        }).catch(error => {
            console.log(error)
        })
    })

});