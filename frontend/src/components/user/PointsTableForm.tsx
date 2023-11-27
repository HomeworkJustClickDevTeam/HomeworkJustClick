import { Button, Table } from "../../types/Table.model"
import { ChangeEvent, FormEvent, useEffect, useState } from "react"
import { createEvaluationPanelService } from "../../services/postgresDatabaseServices"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"

export default function PointsTableForm(props: {
  setIsFormHidden: (isFormHidden: boolean) => void
  table?: Table
}) {
  const userState = useAppSelector(selectUserState)
  const [isEditForm, setIsEditForm] = useState<boolean>(false)
  const [table, setTable] = useState<Table>(new Table("", [], 0, userState?.id))
  useEffect(() => {
    if (props.table) {
      setTable(props.table)
      setTable((prevState) => ({
        ...prevState,
        userId: userState?.id,
      }))
      setIsEditForm(true)
    }
  }, [])

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault()
    if (!isEditForm) {
      createEvaluationPanelService(table)
        .then(() => resetAndHideForm())
        .catch((error) => console.log(error))
    } else {
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

  const deleteEvaluationTable = () => {}

  const getPoints = (points: Button[]): string => {
    if (points) {
      const pointsNumber = points.map((button) => button.points)
      return pointsNumber.join(", ")
    }
    return ""
  }

  return (
    <div>
      <h1>NOWA TABELA</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Nazwa:
          <input
            name="name"
            type="text"
            onChange={handleValueChange}
            value={isEditForm ? table.name : undefined}
          />
        </label>
        <label>Punkty: </label>
        <input
          name="buttons"
          type="text"
          onChange={handlePointsTableChange}
          value={isEditForm ? getPoints(table.buttons) : undefined}
        />
        <label>
          Wiersze w tabeli (max 5):
          <input
            type="number"
            name="width"
            max="5"
            onChange={handleValueChange}
            value={isEditForm ? table.width : undefined}
          />
        </label>
        <button
          type="submit"
          className="mr-6 mt-4 px-10 py-1 rounded-lg bg-main_blue text-white hover:bg-hover_blue hover:shadow-md active:shadow-none"
        >
          {isEditForm ? "Edytuj" : "Zapisz"}
        </button>
      </form>
      {!isEditForm ? (
        <button
          onClick={() => handleCancel()}
          className="mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white"
        >
          {" "}
          X{" "}
        </button>
      ) : (
        <button onClick={() => deleteEvaluationTable()}> Usuń</button>
      )}
    </div>
  )
}
