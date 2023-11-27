import { setupServer } from "msw/node";
import { homePageHandlers } from "../../mocks/handlers";
import '@testing-library/jest-dom';
import { BrowserRouter } from "react-router-dom";
import HomePage from "../../components/home/HomePage";
import renderer from "react-test-renderer";
import { Provider } from "react-redux";
import { setupStore } from "../../redux/store";
import React from "react";

const server = setupServer(...homePageHandlers)
beforeAll(() => server.listen())
beforeEach(()=> {
  jest.clearAllMocks()
})
afterEach(()=> server.resetHandlers())
afterAll(() => server.close())


it("checks if home page renders correctly with sample groups",  () => {
    const renderedHomePage = renderer
      .create(
        <Provider store={setupStore({userState: {
            token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZGFtX25vd2FrQGdtYWlsLmNvbSIsImlhdCI6MTcwMTA2NjIyOCwiZXhwIjoxNzAxMDY5ODI4fQ.gIqB5eSqKchKtFYs17GskZALXXtpVpSFfnJehuSfRNt3EVYipbT_UNFH09NaGUvGtAK-Mq4SukhrZoJW_F8eVw",
            id: 3,
            role: "USER",
            color: 2,
            name: "Adam",
            lastname: "Nowak",
            index: 222222
          }})}>
          <BrowserRouter>
            <HomePage></HomePage>
          </BrowserRouter>
        </Provider>
      )
    expect(renderedHomePage).toMatchSnapshot()
  })



