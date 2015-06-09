package com.github.sorhus.hmmongo.scalatra

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

/**
 * args == {pi, A, B, T}
 */
object Main extends App {
  val port = 8080
  val server = new Server(port)
  val context = new WebAppContext()
  context.setContextPath("/")
  context.setResourceBase("src/main/webapp")
  context.addEventListener(new ScalatraListener)
  context.addServlet(classOf[DefaultServlet], "/")
  context.setAttribute("pi",args(0))
  context.setAttribute("A",args(1))
  context.setAttribute("B",args(2))
  context.setAttribute("T",args(3))
  server.setHandler(context)
  server.start()
  server.join()
}
