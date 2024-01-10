import {useEffect, useState} from "react";
import {FileInterface} from "../../types/FileInterface";
import {
  AdvancedEvaluationExtensionTypeImg,
  AdvancedEvaluationExtensionTypeTxt
} from "../../types/AdvancedEvaluationExtensionType";

export const useDetermineFileType =(file:FileInterface|undefined) => {
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [fileText, setFileText] = useState<string|undefined>(undefined)

  useEffect(() => {
    if(file && AdvancedEvaluationExtensionTypeTxt.includes('.' + file.format)){
      file.data.text()
        .then((text) => {
          setFileText(text)})
    }
    else if(file && AdvancedEvaluationExtensionTypeImg.includes('.' + file.format)){
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