import axios, { AxiosRequestConfig } from "axios"
import { getUser } from "./otherServices"

const mongoDatabaseServices = axios.create({
  baseURL: "http://homework_just_click_mongo_api:8082/api/",
  timeout: 8082,
  headers: {
    "Content-Type": `multipart/form-data`,
    "Access-Control-Allow-Origin": "http://localhost:3000",
    ...(getUser()?.token && {
      Authorization: `Bearer ${getUser()?.token}`,
    }),
  },
})

export const postFileMongoService = (file: FormData) => {
  return mongoDatabaseServices.post("file", file)
}

export const getFileMongoService = (
  mongoId: string,
  config?: AxiosRequestConfig
) => {
  return mongoDatabaseServices.get(`file/${mongoId}`, config)
}
