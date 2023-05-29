import {ChangeEvent, useState} from "react";
import file_sender from "../../services/file-sender";

function AddSolution(){
    const [file,setFile] = useState<File>()
    function handleChangeFile(e:ChangeEvent<HTMLInputElement>) {
        if(e.target.files){
            setFile(e.target.files[0])
        }
    }

    function handleUploadClick() {
        if(!file){
            return
        }
        file_sender.post('file',file).then(r =>console.log(r.data)).catch(e =>console.log(e))
    }

    return(
        <div>
            <input type="file" onChange={handleChangeFile}/>
            <div> {file && `${file.name} - ${file.type}` }</div>
            <button onClick={handleUploadClick}>Wyslij zadanie</button>
        </div>
    )

}
export default AddSolution