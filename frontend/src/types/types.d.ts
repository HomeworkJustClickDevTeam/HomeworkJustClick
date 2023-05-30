import {Dispatch, SetStateAction} from "react";

interface RegisterUser {
    firstname: string,
    lastname: string,
    email: string,
    password: string
}

interface LoginUser {
    email: string,
    password: string
}

type Group = {
    id: number,
    name: string,
    description: string,
    color: number
}

interface GroupCreate {
    name: string,
    description: string,
}

interface UserToShow {
    firstname: string,
    lastname: string,
    email: string,
    index: number,
    id: number,
}

interface InGroup {
    student: boolean,
    teacher: boolean
}

interface PropsForLogin  {
   state:applicationState
}

interface PropsForGroupItem {
    group: Group,
    key: number
}
type PropsForFiltering = {
    setGroups: (groups: Group []) => void
}

interface GroupProp {
    id:string | undefined

}
type Assigment = {
    title:string,
    visible: boolean,
    taskDescription:string,
    completionDatetime: Date,
    id:number
}
interface AssigmentToSend {
    title:string,
    visible: boolean,
    taskDescription:string,
    completionDatetime: Date,
}
interface AssigmentItemProps {
    assigment: Assigment;
    idGroup: string
}
interface UserInGroupProp{
    id:string,
    role:InGroup
}
interface UserItemToDisplay{
    userToShow: UserToShow
}
type FileRespondMongo = {
    id : string,
    name: string,
    format:string
}
type SolutionToSend = {
    creationDatetime:string,
    lastModifiedDatetime: string,
    comment:string
}
interface userState{
    token: string,
    userId: string,
}
interface applicationState{
    loggedIn: boolean,
    homePageIn: boolean,
    userState: userState
}
type Action =
    | { type: 'login'; data: User }
    | { type: 'logout' }
    | {type:'homePageOut'}
    | {type:'homePageIn'}

interface groupContext{
    role:string
    setRole: Dispatch<SetStateAction<string>>
}