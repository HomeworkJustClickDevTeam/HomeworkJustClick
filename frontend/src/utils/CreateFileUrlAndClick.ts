import {FileInterface} from "../types/FileInterface";

export const CreateFileUrlAndClick = (file:FileInterface) =>{
  const fileUrl = URL.createObjectURL(file.data)
  const link = document.createElement("a")
  link.href = fileUrl
  link.download = file.name
  link.click()
  link.remove()
  URL.revokeObjectURL(fileUrl)
}