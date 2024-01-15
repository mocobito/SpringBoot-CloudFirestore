package com.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.model.Employee;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

@RestController
@RequestMapping("employee")
@Log4j2
public class DemoController {

    @GetMapping()
    public List<Employee> getAllEmployees() throws InterruptedException, ExecutionException {
        List<Employee> empList = new ArrayList<>();
        CollectionReference employee = FirestoreClient.getFirestore().collection("Employee");
        ApiFuture<QuerySnapshot> querySnapshot = employee.get();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            Employee emp = doc.toObject(Employee.class);
            empList.add(emp);
        }
        log.info(RequestMethod.GET.name(), "employee", null, empList);
        return empList;
    }

    @PostMapping()
    public Object saveEmployee(@RequestBody Employee employee) {
        CollectionReference employeeCR = FirestoreClient.getFirestore().collection("Employee");
        employeeCR.document(String.valueOf(employee.getId())).set(employee);
        log.info(RequestMethod.POST.name(), "employee", employee, null);
        return ResponseEntity.status(HttpStatus.OK)
                .body(employee);
    }
}
