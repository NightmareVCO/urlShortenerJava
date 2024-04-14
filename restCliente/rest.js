/*
Mini demo de un cliente REST con Node.js
Para ejecutar este archivo, es necesario tener instalado Node.js
Para instalar Node.js, descargar el instalador desde la página oficial:
https://nodejs.org/es/ y seguir las instrucciones de instalación.

Para instalar las dependencias, ejecutar el siguiente comando:
npm install

Para ejecutar este archivo, ejecutar el siguiente comando:
node rest
o el script que se haya definido en el package.json
npm run run
*/

import axios from 'axios';
import to from './to.js';
const url = 'http://localhost:3000/';



// ### GET get all links
// GET http://localhost:3000/links/johndoe
// Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaXNzIjoiYXV0aDAiLCJleHAiOjE3MTMxMzMxMTYsImlhdCI6MTcxMzEzMTMxNn0.QQ_gmVHHsidAnhjHWLXZoiBNSWV1MGs8cwyTu4Ujivs

// ### POST create url
// POST http://localhost:3000/createLink
// Content-Type: application/json

// {
//     "url": "https://www.google.com"
// }

const config = {
  headers: {
    Authorization: 'Bearer ' + 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiaXNzIjoiYXV0aDAiLCJleHAiOjE3MTMxMzUzNzksImlhdCI6MTcxMzEzMzU3OX0.a9Dhtsg3WowZ6IFwvffkU60H51dQ1sFQzgpbnfHNoRI' // reemplace 'token' con su token real
  }
}

const getUserLinks = async (username) => {
  const [error, response] = await to(axios.get(`${url}links/${username}`, config));
  if (error) {
    console.error("error", error.data);
    return;
  }
  console.log("links response:", JSON.stringify(response.data, null, 2));
}

const postLink = async (link) => {
  const [error, response] = await to(axios.post(`${url}createLink`, { url: link }, config));
  if (error) {
    console.error("error", error.data);
    return;
  }
  console.log("links response:", JSON.stringify(response.data, null, 2));
}

console.log("Elige una opción:");
console.log("1. Obtener links de un usuario");
console.log("2. Crear un link");


const stdin = process.openStdin();
stdin.addListener("data", (d) => {
  const option = d.toString().trim();
  switch (option) {
    case "1":
      getUserLinks("johndoe");
      break;
    case "2":
      stdin.addListener("data", (link) => {
        postLink(link.toString().trim());
        stdin.pause();
      });
      break;
    default:
      console.log("Opción no válida");
  }
  console.clear();
});

