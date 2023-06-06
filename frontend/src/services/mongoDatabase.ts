import axios from "axios"

const mongoDatabase = axios.create({
  baseURL: "http://localhost:8082/api/",
  timeout: 8082,
  headers: {
    "Content-Type": `multipart/form-data`,
    "Access-Control-Allow-Origin": "http://localhost:3000",
    ...(localStorage.getItem("token") && {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    }),
  },
})
export default mongoDatabase
