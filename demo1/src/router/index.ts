import { createRouter, createWebHashHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import DeptView from '../views/DeptView.vue'
import EmpView from '../views/EmpView.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: LoginView,
      meta: { title: '登录' }
    },
    {
      path: '/',
      redirect: '/dept'
    },
    {
      path: '/dept',
      name: 'Dept',
      component: DeptView,
      meta: { title: '部门管理', requiresAuth: true }
    },
    {
      path: '/emp',
      name: 'Emp',
      component: EmpView,
      meta: { title: '员工管理', requiresAuth: true }
    }
  ]
})

/** 路由守卫：未登录时跳转到登录页 */
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
