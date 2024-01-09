import { Button, Table } from "../../types/Table.model"
import { ChangeEvent, FormEvent, useEffect, useState } from "react"
import {
  createEvaluationPanelService,
  deleteEvaluationPanelService,
  updateEvaluationPanelService,
} from "../../services/postgresDatabaseServices"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { AxiosResponse } from "axios"

export default function PointsTableForm(props: {
  setIsFormHidden: (isFormHidden: boolean) => void
  tables: Table[] | undefined
  setEvaluationTable: (evaluationTable: Table[]) => void
  table?: Table
}) {
  const userState = useAppSelector(selectUserState)
  const [isEditForm, setIsEditForm] = useState<boolean>(false)
  const [pointsInString, setPointsInString] = useState<string>("")
  const [table, setTable] = useState<Table>(new Table("", [], 0, userState?.id))
  useEffect(() => {
    if (props.table) {
      setTable(props.table)
      setTable((prevState) => ({
        ...prevState,
        userId: userState?.id,
      }))
      setPointsInString(getPoints(props.table.buttons))
      setIsEditForm(true)
    }
  }, [])

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    if (!isEditForm) {
      createEvaluationPanelService(table)
        .then((response: AxiosResponse<Table>) => {
          table.id = response.data.id
          updateTableView()
          resetAndHideForm()
        })
        .catch((error) => console.log(error))
    } else {
      updateEvaluationPanelService(table)
        .then(() => {
          updateTableView()
          resetAndHideForm()
        })
        .catch(() => {})
    }
  }
  const handleValueChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setTable((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handlePointsTableChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    const numbersArray = value
      .split(/[ ,]+/)
      .map((number) => parseFloat(number))
      .filter((number) => !isNaN(number))

    const buttonArray = numbersArray.map((number) => new Button(number))

    if (isEditForm) {
      setPointsInString(value)
    }

    setTable((prevState) => ({
      ...prevState,
      [name]: buttonArray,
    }))
  }

  const handleCancel = () => {
    resetAndHideForm()
  }

  const resetAndHideForm = () => {
    setTable(new Table("", [], 0, 0))
    props.setIsFormHidden(true)
  }

  const deleteEvaluationTable = () => {
    if (table.id) {
      deleteEvaluationPanelService(table.id)
        .then(() => {
          deleteTableFromView()
        })
        .catch(() => {})
    }
  }

  const getPoints = (points: Button[]): string => {
    if (points) {
      const pointsNumber = points.map((button) => button.points)
      return pointsNumber.join(", ")
    }
    return ""
  }

  const updateTableView = () => {
    let newTable: Table[] | undefined = props.tables
    if (isEditForm) {
      newTable = newTable?.map((tableFromList) =>
        tableFromList.id === table.id ? table : tableFromList
      )
    } else {
      newTable?.push(table)
    }
    props.setEvaluationTable(newTable as Table[])
  }

  const deleteTableFromView = () => {
    let newTable = props.tables
    newTable = newTable?.filter(
      (tableFromTables) => tableFromTables.id !== table.id
    )
    props.setEvaluationTable(newTable as Table[])
  }

  return (
    <div className={isEditForm ? 'border-t border-border_gray w-full my-2 pt-1':'mt-6 border-2 border-border_gray p-2 rounded-md max-w-[500px]'}>
      {isEditForm ? <h1 className='font-semibold mb-2'>EDYTUJ TABELĘ</h1> : <h1 className='font-semibold mb-2'>NOWA TABELA</h1>}
      <div className='flex relative'>
      <form onSubmit={handleSubmit} className='flex flex-col gap-2'>
        <label>
          Nazwa:
          <input
            name="name"
            type="text"
            maxLength={255}
            onChange={handleValueChange}
            value={isEditForm ? table.name : undefined}
            className='ml-1 pl-1 border-b solid black focus:outline-none focus:border-b-main_blue'
          />
        </label>
        <label>Punkty:
        <input
          name="buttons"
          type="text"
          onChange={handlePointsTableChange}
          value={isEditForm ? pointsInString : undefined}
          className='ml-1 pl-1 border-b solid black focus:outline-none focus:border-b-main_blue'
        />
        </label>
        <label className='pb-2'>
          Wiersze w tabeli (max 5):
          <input
            type="number"
            name="width"
            max="5"
            onChange={handleValueChange}
            value={isEditForm ? table.width : undefined}
            className='ml-1 pl-1 border-b solid black w-10 focus:outline-none focus:border-b-main_blue'
          />
        </label>
        <button
          type="submit"
          className="absolute right-0 bottom-0 w-24 text-center justify-center px-2 py-1 mr-2 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
        >
          {isEditForm ? "Zatwierdź" : "Prześlij"}
        </button>
      </form>
      {!isEditForm ? (
        <button
          onClick={() => handleCancel()}
          className="absolute top-[-30px] right-[-10px] mr-6 px-4 py-1 rounded-lg bg-berry_red text-white"
        >
          {" "}
          X{" "}
        </button>
      ) : (
        <button onClick={() => deleteEvaluationTable()} className="absolute top-[-30px] right-[-10px] mr-6 px-4 py-1 rounded-lg bg-berry_red text-white"> Usuń</button>
      )}
      </div>
    </div>
  )
}
