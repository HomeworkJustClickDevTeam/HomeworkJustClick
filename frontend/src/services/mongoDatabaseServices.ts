import axios, {AxiosRequestConfig} from "axios"

const mongoDatabaseServices = axios.create({
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

export const postFileMongoService = (file: FormData) => {
  return mongoDatabaseServices.post("file", file)
}

export const getFileMongoService = (mongoId: string, config?: AxiosRequestConfig) => {
  return mongoDatabaseServices.get(
    `file/${mongoId}`,
    config
  )
}
