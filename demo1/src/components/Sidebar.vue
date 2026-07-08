<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2>后台管理系统</h2>
    </div>
    <nav class="sidebar-nav">
      <router-link to="/dept" class="nav-item" active-class="active">
        <span class="nav-icon">📁</span>
        <span class="nav-text">部门管理</span>
      </router-link>
      <router-link to="/emp" class="nav-item" active-class="active">
        <span class="nav-icon">👤</span>
        <span class="nav-text">员工管理</span>
      </router-link>
    </nav>
  </aside>
</template>

<script setup lang="ts">
// Sidebar component
</script>

<style scoped>
.sidebar {
  width: 220px;
  min-width: 220px;
  height: 100vh;
  background: #1a1a2e;
  color: #eee;
  display: flex;
  flex-direction: column;
  user-select: none;
}

.sidebar-header {
  padding: 20px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}

.sidebar-nav {
  flex: 1;
  padding: 12px 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  color: #a0a3b1;
  text-decoration: none;
  font-size: 15px;
  transition: all 0.2s;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #e0e0e0;
}

.nav-item.active {
  background: #4361ee;
  color: #fff;
}

.nav-icon {
  font-size: 18px;
  line-height: 1;
}

.nav-text {
  line-height: 1;
}
</style>
