import {useEffect, useState} from "react";
import {FileInterface} from "../../types/FileInterface";

export const useDetermineFileType =(file:FileInterface|undefined) => {
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [fileText, setFileText] = useState<string|undefined>(undefined)

  useEffect(() => {
    if(file && file.format !== "jpg" && file.format !== "png"){
      file.data.text()
        .then((text) => {
          setFileText(text)})
    }
    else if(file){
      let imageTemp = document.createElement("img")
      imageTemp.src = URL.createObjectURL(file.data)
      imageTemp.onload = () => {
        setImage(imageTemp)
      }
      return () => URL.revokeObjectURL(imageTemp.src)
    }
  }, [file])

  return {image, fileText}
}