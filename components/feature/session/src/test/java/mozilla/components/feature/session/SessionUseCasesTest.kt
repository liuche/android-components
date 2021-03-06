/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.feature.session

import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.engine.EngineSession
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SessionUseCasesTest {
    private val sessionManager = mock(SessionManager::class.java)
    private val engine = mock(Engine::class.java)
    private val selectedEngineSession = mock(EngineSession::class.java)
    private val selectedSession = mock(Session::class.java)
    private val sessionProvider = mock(SessionProvider::class.java)
    private val useCases = SessionUseCases(sessionProvider, engine)

    @Before
    fun setup() {
        `when`(sessionProvider.sessionManager).thenReturn(sessionManager)
        `when`(sessionProvider.selectedSession).thenReturn(selectedSession)
        `when`(sessionProvider.getOrCreateEngineSession(engine)).thenReturn(selectedEngineSession)
    }

    @Test
    fun testLoadUrl() {
        useCases.loadUrl.invoke("http://mozilla.org")
        verify(selectedEngineSession).loadUrl("http://mozilla.org")
    }

    @Test
    fun testReload() {
        val engineSession = mock(EngineSession::class.java)
        val session = mock(Session::class.java)
        `when`(sessionProvider.getEngineSession(session)).thenReturn(engineSession)

        useCases.reload.invoke(session)
        verify(engineSession).reload()

        `when`(sessionProvider.getEngineSession(selectedSession)).thenReturn(null)
        useCases.reload.invoke()
        verify(selectedEngineSession, never()).reload()

        `when`(sessionProvider.getEngineSession(selectedSession)).thenReturn(selectedEngineSession)
        useCases.reload.invoke()
        verify(selectedEngineSession).reload()
    }

    @Test
    fun testGoBack() {
        useCases.goBack.invoke()
        verify(selectedEngineSession).goBack()
    }

    @Test
    fun testGoForward() {
        useCases.goForward.invoke()
        verify(selectedEngineSession).goForward()
    }
}
